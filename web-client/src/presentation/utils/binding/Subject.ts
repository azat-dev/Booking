import {SubjectCallback} from "./SubjectCallback";
import {Cancellable} from "./Cancellable";

export interface ReadonlySubject<Value> {
    value: Value;

    listen(callback: SubjectCallback<Value>): Cancellable;

    stopListening(callback: SubjectCallback<Value>): void;
}

export default abstract class Subject<Value> implements ReadonlySubject<Value> {

     value: Value;

    public constructor(value: Value) {
        this.value = value;
    }

    abstract set(newValue: Value): void;

    abstract listen(callback: SubjectCallback<Value>): Cancellable;

    abstract stopListening(callback: SubjectCallback<Value>): void;

    abstract dispose(): void;
}
