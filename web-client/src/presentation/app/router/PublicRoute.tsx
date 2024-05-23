import React from "react";

import RouteItem from "./RouteItem";
import {SessionState} from "../../../domain/auth/entities/AppSession";
import Subject from "../../utils/binding/Subject.ts";
import PublicRouteComponent from "./PublicRouteComponent.tsx";

class PublicRoute implements RouteItem {

    public constructor(
        public readonly path: string,
        private readonly View: React.ComponentType<any>
    ) {
    }

    public view = async (session: Subject<SessionState>, params: any): Promise<React.ReactElement> => {
        return (
            <PublicRouteComponent
                params={params}
                session={session}
                View={this.View}
            />
        );
    }

}

export default PublicRoute;