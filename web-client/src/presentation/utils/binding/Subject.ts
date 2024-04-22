import { SubjectCallback } from "./SubjectCallback";
import { Cancellable } from "./Cancellable";

export default abstract class Subject<Value> {
    value: Value;
    abstract set(newValue: Value): void;
    abstract listen(callback: SubjectCallback<Value>): Cancellable;
    abstract stopListening(callback: SubjectCallback<Value>): void;

    public constructor(value: Value) {
        this.value = value;
    }
}
