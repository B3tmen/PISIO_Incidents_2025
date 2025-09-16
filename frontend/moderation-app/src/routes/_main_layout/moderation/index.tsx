import { tokenService } from '@api/services/tokenService';
import ModerationPage from '@pages/ModerationPage/ModerationPage';
import { createFileRoute, redirect } from '@tanstack/react-router'

export const Route = createFileRoute('/_main_layout/moderation/')({
  beforeLoad: ({ location }) => {
    const token = tokenService.getAccessToken();
    if (!token) {
      throw redirect({
        to: '/login',
        search: {
          // optionally send them back after login
          redirect: location.href,
        },
      })
    }
  },
  component: RouteComponent,
})

function RouteComponent() {
  return (
    <ModerationPage />
  );
}
