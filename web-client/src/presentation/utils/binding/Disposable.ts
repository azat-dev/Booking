export default interface Disposable {

    dispose(): void;

    connect(disposable: Disposable): void;

    doUntilDisposed(callback: () => Disposable): void;
}