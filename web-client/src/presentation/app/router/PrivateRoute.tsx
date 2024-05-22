import React from "react";

import RouteItem from "./RouteItem";
import {SessionState} from "../../../domain/auth/entities/AppSession";
import Subject from "../../utils/binding/Subject.ts";
import PrivateRouteComponent from "./PrivateRouteComponent.tsx";

class PrivateRoute implements RouteItem {

    public constructor(
        public readonly path: string,
        private readonly redirectTo: string,
        private readonly AuthProcessingView: React.ComponentType<any>,
        private readonly AuthenticatedView: React.ComponentType<any>
    ) {
    }

    public view = async (session: Subject<SessionState>, params: any): Promise<React.ReactElement> => {
        return (
            <PrivateRouteComponent
                params={params}
                session={session}
                redirectTo={this.redirectTo}
                AuthProcessingView={this.AuthProcessingView}
                AuthenticatedView={this.AuthenticatedView}
            />
        );
    }

}

export default PrivateRoute;