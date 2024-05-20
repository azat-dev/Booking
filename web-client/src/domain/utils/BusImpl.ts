import value from "../../presentation/utils/binding/value";
import Bus, {Cancelable} from "./Bus";
import Subject from "../../presentation/utils/binding/Subject";
import AppEvent from "./AppEvent";
import Command from "./Command";

class BusImpl implements Bus {
    private eventsStream: Subject<any> = value(null);


    publish = (event: AppEvent | Command): void => {
        console.log("AppEvent: ", event.type, event);
        this.eventsStream.set(event);
    }

    subscribe = (handler: (event: any) => Promise<void>): Cancelable => {
        return this.eventsStream.listen((event) => {
            handler(event);
        });
    }

    waitFor = async <T>(condition: (event: any) => Promise<T>): Promise<T> => {
        return new Promise<T>(resolve => {
            const ref = {} as any;
            ref.c = this.subscribe(async event => {
                const result = await condition(event);
                ref.c.cancel();
                resolve(result);
            });
        });
    }

    publishAndWaitForResponse = async <T>(event: any, condition: (event: any) => Promise<T>): Promise<T> => {

        const result = await this.waitFor(condition);
        this.publish(event);
        return result;
    }
}

export default BusImpl;