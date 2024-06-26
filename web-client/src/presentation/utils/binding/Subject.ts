import {SubjectCallback} from "./SubjectCallback";
import Disposable from "./Disposable";

export interface ReadonlySubject<Value>  extends Disposable {
    value: Value;

    listen(callback: SubjectCallback<Value>): Disposable;

    stopListening(callback: SubjectCallback<Value>): void;
}

export default abstract class Subject<Value> implements ReadonlySubject<Value> {

     value: Value;

    public constructor(value: Value) {
        this.value = value;
    }

    abstract set(newValue: Value): void;

    abstract listen(callback: SubjectCallback<Value>): Disposable;

    abstract stopListening(callback: SubjectCallback<Value>): void;

    abstract dispose(): void;
}
