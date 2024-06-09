import React from "react";
import {SessionState} from "../../../domain/auth/entities/AppSession";
import Subject from "../../utils/binding/Subject";

export type RouteComponentProvider = (
    session: Subject<SessionState>,
    params: any
) => Promise<React.ReactElement>;

abstract class RouteItem {
    abstract readonly path: string;
    abstract readonly view: RouteComponentProvider;
}

export default RouteItem;