export type SingletonFactory<T> = () => T;

export default function singleton<T>(factory: SingletonFactory<T>): () => T {
    let instance: T | undefined;

    return function getInstance(): T {
        if (!instance) {
            instance = factory();
        }

        return instance;
    };
}


