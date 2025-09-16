import { Flex, message, Typography } from "antd";
import { useNavigate } from "@tanstack/react-router";
import { GoogleLogin, type CredentialResponse } from "@react-oauth/google";
import { authService } from "@api/services/authService";

import './LoginPage.css'
import type { AuthenticationResponse } from "@types/index";
import { tokenService } from "@api/services/tokenService";

function LoginPage() {
    const navigate = useNavigate();

    const onLoginSuccess = async (credentialResponse: CredentialResponse) => {
        try {
            console.log("Authenticating...: ", credentialResponse);
            const idToken = credentialResponse.credential!;
            
            const authResponse: AuthenticationResponse = await authService.googleLogin(idToken);
            
            tokenService.setAccessToken(authResponse.accessToken)
           
            navigate({ to: '/moderation' });
        }
        catch (error) {
            console.error("Google login error:", error);
        }
    }

    const onLoginError = () => {
        console.log("ERROR: LOGIN FAILED");
    }

    return (
        <Flex id="root" align="center" justify="center" vertical>
            <Flex align="center" justify="space-between" vertical className="login-container" >
                <Flex align="center" justify="space-between" vertical>
                    <Typography.Title level={2}>- Moderation App -</Typography.Title>
                    <Typography.Title>Login</Typography.Title>
                </Flex>
                
                
                <GoogleLogin 
                    text="signin_with"
                    onSuccess={onLoginSuccess} 
                    onError={onLoginError}
                    auto_select={true} 
                />
            </Flex>
        </Flex>
    );
}

export default LoginPage;