import ActivePageVM from "./ActivePageVM";

export default interface PropsActivePage {
    vm: ActivePageVM;
    views: Record<string, React.ComponentType<{vm: any}>>
}