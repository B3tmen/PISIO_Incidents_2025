import { createFileRoute, useNavigate } from '@tanstack/react-router'

export const Route = createFileRoute('/')({
  component: RouteComponent,
})

function RouteComponent() {
  const navigate = useNavigate();

  const navigateToLogin = () => {
    navigate({ to: '/login' });
  }
  
  return (
    <>
      {navigateToLogin()}
    </>
  );
}
