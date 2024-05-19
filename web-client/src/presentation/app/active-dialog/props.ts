import ActiveDialogVM from "./ActiveDialogVM";

export default interface PropsActiveDialog {
    vm: ActiveDialogVM
    views: Record<string, React.ComponentType<any>>
}