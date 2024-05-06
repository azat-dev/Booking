import React, { useState } from "react";
import Subject from "../../../utils/binding/Subject";
import value from "../../../utils/binding/value";

class FormInputViewModel {
    public readonly isDisabled: Subject<boolean>;
    public readonly text: Subject<string | undefined>;
    public readonly isWrong: Subject<boolean>;
    public readonly errorText: Subject<string | undefined>;

    public constructor(
        initialValue: string | undefined,
        private readonly onChange: (value: string) => void,
        initialIsDisabled: boolean = false,
        initialIsWrong: boolean = false,
        initialErrorText?: string | undefined
    ) {
        this.text = value(initialValue);
        this.isDisabled = value(initialIsDisabled);
        this.isWrong = value(initialIsWrong);
        this.errorText = value(initialErrorText);
    }

    public updateValue = (value: string) => {
        this.text.set(value);
    };

    public clear = () => {
        this.text.set(undefined);
    };

    public getValue = (): string | undefined => {
        return this.text.value;
    };

    public change = (value: string) => {
        this.onChange(value);
    };

    public updateIsDisabled = (isDisabled: boolean) => {
        this.isDisabled.set(isDisabled);
    };

    public updateIsWrong = (isWrong: boolean) => {
        this.isWrong.set(isWrong);
    };

    public updateErrorText = (errorText: string | undefined) => {
        this.errorText.set(errorText);
    };
}

export default FormInputViewModel;
