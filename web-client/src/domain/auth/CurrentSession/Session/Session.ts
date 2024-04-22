import SessionAnonymous from "./SessionAnonymous";
import SessionAuthProcessing from "./SessionAuthProcessing";
import SessionAuthenticated from "./SessionAuthenticated";

export type Session =
    | SessionAnonymous
    | SessionAuthProcessing
    | SessionAuthenticated;
