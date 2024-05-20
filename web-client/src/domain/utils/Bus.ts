export interface Cancelable {
    cancel(): void;
}

interface Bus {

    publish(event: any): void;

    publishAndWaitForResponse<T>(event: any, condition: (event: any) => Promise<T>): Promise<T>;

    subscribe(handler: (event: any) => Promise<void>): Cancelable;
}

export default Bus;