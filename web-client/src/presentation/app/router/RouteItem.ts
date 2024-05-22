import React from "react";
import {SessionState} from "../../../domain/auth/entities/AppSession";
import AppSessionAnonymous from "../../../domain/auth/entities/AppSessionAnonymous";
import Subject from "../../utils/binding/Subject.ts";


export class RedirectRouteException extends Error {
    constructor(public readonly path: string, replace: boolean = false) {
        super();
    }

}

export type RouteComponentProvider = (session: Subject<SessionState>, params: any) => Promise<React.ReactElement>;

abstract class RouteItem {
    abstract readonly path: string;
    abstract readonly view: RouteComponentProvider;
}

export default RouteItem;