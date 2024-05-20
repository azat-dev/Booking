import Disposable from "./Disposable";

class Disposables {

    private disposables: Disposable[] = [];

    public add = (disposable: Disposable) => {
        this.disposables.push(disposable);
    }

    public dispose = () => {
        this.disposables.forEach(d => d.dispose());
        this.disposables = [];
    }
}

export default Disposables;