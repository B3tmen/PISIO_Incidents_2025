import { createFileRoute, Outlet } from "@tanstack/react-router";
import React from "react";

export const Route = createFileRoute('/_main_layout')({
    component: RouteComponent,
})

function RouteComponent() {
    return (
        <React.Fragment>
            <Outlet />
        </React.Fragment>
    );
}