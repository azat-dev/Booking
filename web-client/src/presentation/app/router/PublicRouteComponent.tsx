import useUpdatesFrom from "../../utils/binding/useUpdatesFrom.ts";
import Subject from "../../utils/binding/Subject.ts";
import {SessionState} from "../../../domain/auth/entities/AppSession";
import AppSessionAuthenticated from "../../../domain/auth/entities/AppSessionAuthenticated";


interface PropsAuthenticatedView {
    session: AppSessionAuthenticated;
    params: any;
}

export interface PropsPrivateRouteComponent {
    params: any;
    session: Subject<SessionState>;
    View: React.ComponentType<PropsAuthenticatedView>;
}

const PublicRouteComponent = (props: PropsPrivateRouteComponent) => {
    const {
        params,
        session,
        View
    } = props;
    const [s] = useUpdatesFrom(session);

    return (
        <View session={s} params={params}/>
    );
}

export default PublicRouteComponent;