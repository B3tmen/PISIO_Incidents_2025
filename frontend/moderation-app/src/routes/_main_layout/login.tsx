import LoginPage from '@pages/LoginPage/LoginPage'
import { createFileRoute } from '@tanstack/react-router'

export const Route = createFileRoute('/_main_layout/login')({
  component: RouteComponent,
})

function RouteComponent() {
  return <LoginPage />
}
