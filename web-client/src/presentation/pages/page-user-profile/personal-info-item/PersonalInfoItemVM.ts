import {ReadonlySubject} from "../../../utils/binding/Subject.ts";

class PersonalInfoItemVM {


    public constructor(
        public readonly text: ReadonlySubject<string>
    ) {
    }
}

export default PersonalInfoItemVM;