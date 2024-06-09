import Disposable from "../../presentation/utils/binding/Disposable";
import AppEvent from "./AppEvent.ts";
import Command from "./Command.ts";

interface Bus {

    publish(event: any): void;

    waitFor(condition: (event: any) => boolean): Promise<any>;

    publishAndWaitFor(event: any, condition: (event: any) => boolean): Promise<any>;

    subscribe(handler: (event: any) => void): Disposable;

    publishCommand(command: Command): Promise<AppEvent>;
}

export const matchTypes = (...types: string[]) => {
    const set = new Set(types);
    return (event: any) => set.has(event.type);
};

export const matchClasses = (...classes: any[]) => {
    const set = new Set(classes as any);
    return (event: (AppEvent | Command)) => set.has(event.constructor);
}

export default Bus;