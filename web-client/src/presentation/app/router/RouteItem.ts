import React from "react";
import {SessionState} from "../../../domain/auth/entities/AppSession";
import Subject from "../../utils/binding/Subject";
import PagesConfig from "../config/PagesConfig.ts";

export type RouteComponentProvider = (
    session: Subject<SessionState>,
    params: any,
    pagesConfig: PagesConfig
) => Promise<React.ReactElement>;

abstract class RouteItem {
    abstract readonly path: string;
    abstract readonly view: RouteComponentProvider;
}

export default RouteItem;