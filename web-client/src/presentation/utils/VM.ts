import KeepType from "../../domain/utils/KeepType.ts";
import Disposables from "./binding/Disposables.ts";
import Bus from "../../domain/utils/Bus.ts";
import AppEvent from "../../domain/utils/AppEvent.ts";
import Disposable from "./binding/Disposable.ts";

let lastId = 0;

abstract class VM extends KeepType implements Disposable {

    public readonly vmId: string = 'vmId';
    public readonly vmDisposables = new Disposables();

    private bus!: Bus;

    public constructor() {
        super();
        this.vmId = this.type + '-' + (lastId++).toString();

        this.dispose.bind(this);
    }

    public cleanOnDestroy = (...disposables: Disposable[]) => {
        disposables.forEach(disposable => {
            this.vmDisposables.add(disposable);
        });
    }

    public dispose ()  {
        this.vmDisposables.dispose();
    }

    public listenOwnEvents = (bus: Bus, callback: (event: AppEvent) => void) => {
        this.vmDisposables.add(bus.subscribe(event => {
            if (event.senderId === this.vmId) {
                callback(event);
            }
        }));
    }

    public listenGlobalEvents = (bus: Bus, callback: (event: AppEvent) => void) => {
        this.vmDisposables.add(bus.subscribe(callback));
    }
}

export default VM;