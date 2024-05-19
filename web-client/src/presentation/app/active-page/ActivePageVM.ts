import Subject from "../../utils/binding/Subject";

export interface PageItem {
    type: string;
    vm?: any;
}

class ActivePageVM {
    public constructor(public readonly page: Subject<PageItem | null>) {
    }
}

export default ActivePageVM;