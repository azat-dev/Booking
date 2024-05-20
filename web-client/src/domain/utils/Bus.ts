import Disposable from "../../presentation/utils/binding/Disposable";

export interface Cancelable {
    cancel(): void;
}

interface Bus {

    publish(event: any): void;

    waitFor(condition: (event: any) => boolean): Promise<any>;

    publishAndWaitFor(event: any, condition: (event: any) => boolean): Promise<any>;

    subscribe(handler: (event: any) => Promise<void>): Disposable;
}

export const matchTypes = (...types: string[]) => {
    const set = new Set(types);
    return (event: any) => set.has(event.type);
};

export default Bus;