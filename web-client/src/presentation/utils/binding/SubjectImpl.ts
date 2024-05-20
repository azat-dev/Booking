import {Cancellable} from "./Cancellable";
import {SubjectCallback} from "./SubjectCallback";
import Subject from "./Subject";

export default class SubjectImpl<Value> extends Subject<Value> {
    private subscribers: Set<SubjectCallback<Value>> = new Set();

    public set = (newValue: Value): void => {
        if (this.value === newValue) {
            return;
        }

        this.value = newValue;
        this.notifySubscribers();
    };

    public listen = (callback: SubjectCallback<Value>): Cancellable => {
        this.subscribers.add(callback);
        return {
            cancel: () => {
                this.subscribers.delete(callback);
            },
        };
    };

    private notifySubscribers = (): void => {
        const value = this.value;
        this.subscribers.forEach((subscriber) => {
            subscriber(value);
        });
    };

    public stopListening(callback: SubjectCallback<Value>): void {
        this.subscribers.delete(callback);
    }

    public dispose = (): void => {
        this.subscribers.clear();
    }
}
