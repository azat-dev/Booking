import value from "../../presentation/utils/binding/value";
import Bus from "./Bus";
import Subject from "../../presentation/utils/binding/Subject";
import AppEvent from "./AppEvent";
import Command from "./Command";
import Disposable from "../../presentation/utils/binding/Disposable";

class BusImpl implements Bus {

    private eventsStream: Subject<any> = value(null);


    publish = (event: AppEvent | Command): void => {
        console.log("%cEVENT: ",  "color: #FF33FF; font-weight: bold;", event.type, event);
        this.eventsStream.set(event);
    }

    subscribe = (handler: (event: any) => Promise<void>): Disposable => {
        return this.eventsStream.listen((event) => {
            handler(event);
        });
    }

    waitFor = async <T>(condition: (event: any) => boolean): Promise<T> => {
        return new Promise<T>(resolve => {
            const ref = {} as any;
            ref.c = this.subscribe(async event => {
                const found = await condition(event);
                if (!found) {
                    return;
                }

                ref.c.dispose();
                resolve(event);
            });
        });
    }

    publishAndWaitFor = async (event: any, condition: (event: any) => boolean): Promise<any> => {

        const promise = this.waitFor(condition);
        this.publish(event);
        return await promise
    }
}

export default BusImpl;