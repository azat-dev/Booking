import {ReadonlySubject} from "./Subject";
import SubjectImpl from "./SubjectImpl";

const mappedValue = <T, M>(source: ReadonlySubject<T>, map: (value: T) => M): ReadonlySubject<M> => {
    const subject = new SubjectImpl(map(source.value));
    source.listen((newValue) => {
        subject.set(map(newValue));
    })
    return subject;
};

export default mappedValue;
