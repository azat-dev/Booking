import FormInputVM from "./FormInputVM";

interface PropsFormInput {
    vm: FormInputVM;
    label: string;
    type: React.HTMLInputTypeAttribute;
    placeholder: string;
}

export default PropsFormInput;
