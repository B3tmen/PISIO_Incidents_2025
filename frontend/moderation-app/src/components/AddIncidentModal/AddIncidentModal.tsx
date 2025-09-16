import { Button, Flex, Image, Input, Modal, Select, Typography, Upload, type GetProp, type UploadFile, type UploadProps } from "antd";
import { useEffect, useState } from "react";
import { faBank, faBoltLightning, faCar, faCarCrash, faFire, faFireBurner, faHelmetSafety, faLanguage, faPeopleRobbery, faPersonHarassing, faPlus, faTree, faWater } from "@fortawesome/free-solid-svg-icons";
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import IncidentTypeCardGroup from "@components/IncidentTypeCard/IncidentTypeCardGroup";
import { EMPTY_INCIDENT_REQUEST } from "@util/constants/emptyObjects";
import { IncidentSubtype, IncidentType } from "@types/enums";
import type { IncidentTypeCardConfig, Location } from "@types/index";
import type { IncidentRequest } from "@types/requests";
import { translationService } from "@api/services/translationService";

const { TextArea } = Input;
type FileType = Parameters<GetProp<UploadProps, 'beforeUpload'>>[0];

const getBase64 = (file: FileType): Promise<string> =>
  new Promise((resolve, reject) => {
    const reader = new FileReader();
    reader.readAsDataURL(file);
    reader.onload = () => resolve(reader.result as string);
    reader.onerror = (error) => reject(error);
  });

interface Props {
    isOpen: boolean;
    onCancel: () => void;
    onSubmit: (incident: IncidentRequest, fileList: UploadFile[]) => void;
    onError?: (msg: string) => void;
    location?: Location | null;
    isUploading: boolean;
}

function AddIncidentModal({ isOpen, location, isUploading, onCancel, onSubmit, onError }: Props) {
    const [incidentRequest, setIncidentRequest] = useState<IncidentRequest>(EMPTY_INCIDENT_REQUEST);
    const [selectedIncidentType, setSelectedIncidentType] = useState<IncidentType | null>(null);
    const [selectedIncidentSubtype, setSelectedIncidentSubtype] = useState<IncidentSubtype | null>(null);
    const [description, setDescription] = useState("");
    const [previewOpen, setPreviewOpen] = useState(false);
    const [previewImage, setPreviewImage] = useState('');
    const [fileList, setFileList] = useState<UploadFile[]>([]);
    const translationOptions = [
        { value: 'en', label: <span>English</span> },
        { value: 'sr', label: <span>Serbian</span> },
    ];
    const [sourceLang, setSourceLang] = useState("");
    const [targetLang, setTargetLang] = useState("");


    useEffect(() => {
        if (location) {
            setIncidentRequest(prev => ({
                ...prev,
                location,
            }));
        }
    }, [location]);

    const handlePreview = async (file: UploadFile) => {
        if (!file.url && !file.preview) {
            file.preview = await getBase64(file.originFileObj as FileType);
        }

        setPreviewImage(file.url || (file.preview as string));
        setPreviewOpen(true);
    };

    const handleChange: UploadProps['onChange'] = ({ fileList: newFileList }) => setFileList(newFileList);

    const uploadButton = (
        <button style={{ border: 0, background: 'none' }} type="button">
            <FontAwesomeIcon icon={faPlus} />
            <div style={{ marginTop: 8 }}>Upload</div>
        </button>
    );

    // Map incident types to their subtypes
    const incidentSubtypesMap: Record<IncidentType, Array<IncidentTypeCardConfig>> = {
        [IncidentType.FIRE]: [
            { id: IncidentSubtype.ARSON, icon: <FontAwesomeIcon icon={faFireBurner} />, title: "Arson" },
            { id: IncidentSubtype.FOREST_FIRE, icon: <FontAwesomeIcon icon={faTree} />, title: "Forest fire" },
        ],
        [IncidentType.CRIME]: [
            { id: IncidentSubtype.CAR_ROBBERY, icon: <FontAwesomeIcon icon={faCar} />, title: "Car robbery" },
            { id: IncidentSubtype.BANK_ROBBERY, icon: <FontAwesomeIcon icon={faBank} />, title: "Bank robbery" },
            { id: IncidentSubtype.ASSAULT, icon: <FontAwesomeIcon icon={faPersonHarassing} />, title: "Assault" },
        ],
        [IncidentType.ACCIDENT]: [
            { id: IncidentSubtype.CAR_ACCIDENT, icon: <FontAwesomeIcon icon={faCarCrash} />, title: "Car accident" },
            { id: IncidentSubtype.ELECTRICAL_ACCIDENT, icon: <FontAwesomeIcon icon={faBoltLightning} />, title: "Electrical accident" },
            { id: IncidentSubtype.CONSTRUCTION_ACCIDENT, icon: <FontAwesomeIcon icon={faHelmetSafety} />, title: "Construction accident" },
        ],
        [IncidentType.FLOOD]: []
    };

    // When selecting type
    const handleIncidentTypeSelect = (type: IncidentType) => {
        setSelectedIncidentType(type);
        setIncidentRequest(prev => ({
            ...prev,
            type,
            subtype: '',
        }));
    };

    // When selecting subtype
    const handleIncidentSubtypeSelect = (subtype: IncidentSubtype) => {
        setSelectedIncidentSubtype(subtype);
        setIncidentRequest(prev => ({
            ...prev,
            subtype,
        }));
    };

    // When typing description
    const handleDescriptionChange = (e: React.ChangeEvent<HTMLTextAreaElement>) => {
        const value = e.target.value;
        setDescription(value);
        setIncidentRequest(prev => ({
            ...prev,
            description: value,
        }));
    };

    const onTranslateText = async (text: string, source: string, target: string) => {
        try{
            const translation = await translationService.translateText(text, source, target);
            console.log(translation);

            setDescription(translation);
        } catch(error) {
            onError?.(error.message);
        }
    }

    const incidentTypeCards = [
        { id: IncidentType.FIRE, icon: <FontAwesomeIcon icon={faFire} />, title: IncidentType.FIRE },
        { id: IncidentType.FLOOD, icon: <FontAwesomeIcon icon={faWater} />, title: IncidentType.FLOOD },
        { id: IncidentType.CRIME, icon: <FontAwesomeIcon icon={faPeopleRobbery} />, title: IncidentType.CRIME },
        { id: IncidentType.ACCIDENT, icon: <FontAwesomeIcon icon={faCarCrash} />, title: IncidentType.ACCIDENT },
    ];

    const modalLocation = incidentRequest.location != undefined ? `
        ${incidentRequest.location?.country.trim() + ', ' || ''}
        ${incidentRequest.location?.state.trim() + ', ' || ''}
        ${incidentRequest.location?.city.trim() + ', ' || ''}
        ${incidentRequest.location?.address.trim() + ', ' || ''}
    `.trim() : null;

    const footer: React.ReactNode = 
        <Flex justify="space-between">
            <Button danger onClick={onCancel}>Cancel</Button>
            <Button color="green" variant="solid" loading={isUploading} onClick={() => onSubmit(incidentRequest, fileList)}>Submit</Button>
        </Flex>
    ;

    return (
        <Modal 
            open={isOpen}
            onCancel={onCancel}
            footer={footer}
            title="Add an incident"
        >
            <Flex className="body-container" vertical gap={12}>
                <Flex className="incident-types" vertical gap={12}>
                    <Typography.Title level={5}>What are you reporting? Select a type</Typography.Title>

                    <IncidentTypeCardGroup 
                        cards={incidentTypeCards}
                        selectedId={selectedIncidentType}
                        onSelect={(id) => handleIncidentTypeSelect(id as IncidentType)}
                    />

                    {selectedIncidentType && (
                        <Flex vertical gap={12}>
                            <Typography.Title level={5}>Select a subtype (optional)</Typography.Title>
                            <IncidentTypeCardGroup 
                                cards={incidentSubtypesMap[selectedIncidentType] ?? []}
                                selectedId={selectedIncidentSubtype}
                                onSelect={(id) => handleIncidentSubtypeSelect(id as IncidentSubtype)}
                            />
                        </Flex>
                        
                    )}
                </Flex>
                
                {/* Location */}
                <Flex className="location" vertical>
                    <Typography.Title level={5}>Location</Typography.Title>
                    <Input value={modalLocation} readOnly />
                </Flex>
                
                {/* Description */}
                <Flex className="description" vertical>
                    <Typography.Title level={5}>Description</Typography.Title>
                    <TextArea
                        value={description}
                        onChange={handleDescriptionChange}
                        placeholder="Please describe the incident..."
                        autoSize={{ minRows: 3, maxRows: 5 }}
                    />

                    {/* Translation */}
                    <Flex justify="space-between">
                        <Select placeholder="From" value={sourceLang} onChange={setSourceLang} options={translationOptions} style={{ width: '100%' }} />
                        <Select placeholder="To" value={targetLang} onChange={setTargetLang} options={translationOptions} style={{ width: '100%' }} />
                    </Flex>
                    <Button 
                        onClick={() => onTranslateText(description, sourceLang, targetLang)}
                        icon={ <FontAwesomeIcon icon={faLanguage} /> }
                    >
                        Translate
                    </Button>
                </Flex>

                <Flex className="photos" vertical>
                    <Typography.Title level={5}>Photos</Typography.Title>

                    <Upload
                        //action="https://660d2bd96ddfa2943b33731c.mockapi.io/api/upload"
                        listType="picture-card"
                        fileList={fileList}
                        onPreview={handlePreview}
                        onChange={handleChange}
                    >
                        {fileList.length >= 8 ? null : uploadButton}
                    </Upload>
                    {previewImage && (
                        <Image
                            wrapperStyle={{ display: 'none' }}
                            preview={{
                                visible: previewOpen,
                                onVisibleChange: (visible) => setPreviewOpen(visible),
                                afterOpenChange: (visible) => !visible && setPreviewImage(''),
                            }}
                            src={previewImage}
                        />
                    )}
                </Flex>
            </Flex>
        </Modal>
    );
}

export default AddIncidentModal;