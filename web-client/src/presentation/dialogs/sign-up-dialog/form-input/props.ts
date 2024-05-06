import FormInputViewModel from "./FormInputViewModel";

interface PropsFormInput {
    vm: FormInputViewModel;
    label: string;
    type: React.HTMLInputTypeAttribute;
    placeholder: string;
}

export default PropsFormInput;
