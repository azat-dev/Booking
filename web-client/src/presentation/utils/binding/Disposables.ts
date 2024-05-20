import Disposable from "./Disposable";

class Disposables {

    private disposables: Disposable[] = [];

    public addItems = (...disposables: Disposable[]) => {
        this.disposables.push(...disposables);
    }

    public add = (disposable: Disposable) => {
        this.disposables.push(disposable);
    }

    public dispose = () => {
        this.disposables.forEach(d => d.dispose());
        this.disposables = [];
    }
}

export default Disposables;