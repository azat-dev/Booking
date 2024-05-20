import {SubjectCallback} from "./SubjectCallback";
import Subject from "./Subject";
import Disposable from "./Disposable";

export default class SubjectImpl<Value> extends Subject<Value> {
    private subscribers: Set<SubjectCallback<Value>> = new Set();

    public set = (newValue: Value): void => {
        if (this.value === newValue) {
            return;
        }

        this.value = newValue;
        this.notifySubscribers();
    };

    public listen = (callback: SubjectCallback<Value>): Disposable => {
        this.subscribers.add(callback);
        return {
            dispose: () => {
                this.subscribers.delete(callback);
            },
        };
    };

    public stopListening(callback: SubjectCallback<Value>): void {
        this.subscribers.delete(callback);
    }

    public dispose = (): void => {
        this.subscribers.clear();
    }

    private notifySubscribers = (): void => {
        const value = this.value;
        this.subscribers.forEach((subscriber) => {
            subscriber(value);
        });
    };
}
