import useUpdatesFrom from "../../utils/binding/useUpdatesFrom.ts";
import Subject from "../../utils/binding/Subject.ts";
import {SessionState} from "../../../domain/auth/entities/AppSession";
import AppSessionAnonymous from "../../../domain/auth/entities/AppSessionAnonymous";
import {Navigate} from "react-router-dom";
import AppSessionLoading from "../../../domain/auth/entities/AppSessionLoading";
import AppSessionAuthenticated from "../../../domain/auth/entities/AppSessionAuthenticated";


interface PropsAuthenticatedView {
    session: AppSessionAuthenticated;
    params: any;
}

export interface PropsPrivateRouteComponent {
    params: any;
    session: Subject<SessionState>;
    redirectTo: string;
    AuthProcessingView: React.ComponentType<any>;
    AuthenticatedView: React.ComponentType<PropsAuthenticatedView>;
}

const PrivateRouteComponent = ({params, session, redirectTo, AuthProcessingView, AuthenticatedView}: PropsPrivateRouteComponent) => {

    const [s] = useUpdatesFrom(session);

    switch (s.constructor) {
        case AppSessionAnonymous:
            return <Navigate to={redirectTo} />
        case AppSessionLoading:
            return <AuthProcessingView />;
        case AppSessionAuthenticated:
            return <AuthenticatedView session={s} params={params} />;
        default:
            throw new Error(`Unknown session type: ${s}`);
    }
}

export default PrivateRouteComponent;