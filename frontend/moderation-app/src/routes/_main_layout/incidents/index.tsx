import IncidentsPage from '@pages/IncidentsPage/IncidentsPage'
import { createFileRoute } from '@tanstack/react-router'

export const Route = createFileRoute('/_main_layout/incidents/')({
  component: RouteComponent,
})

function RouteComponent() {
  return <IncidentsPage />
}
