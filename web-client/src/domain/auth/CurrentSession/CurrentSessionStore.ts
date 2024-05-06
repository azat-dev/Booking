import Subject from "../../../presentation/utils/binding/Subject";
import { Session } from "./Session/Session";

export default interface CurrentSessionStore {
    current: Subject<Session>;

    tryToLoadLastSession(): Promise<void>;
}
