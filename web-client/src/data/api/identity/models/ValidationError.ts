/* tslint:disable */
/* eslint-disable */
/**
 * Users  API
 * Describes the API of Users Endpoint
 *
 * The version of the OpenAPI document: 1.0.0
 * 
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */

import { mapValues } from '../runtime';
import type { ValidationErrorDetails } from './ValidationErrorDetails';
import {
    ValidationErrorDetailsFromJSON,
    ValidationErrorDetailsFromJSONTyped,
    ValidationErrorDetailsToJSON,
} from './ValidationErrorDetails';

/**
 * 
 * @export
 * @interface ValidationError
 */
export interface ValidationError {
    /**
     * 
     * @type {string}
     * @memberof ValidationError
     */
    type: string;
    /**
     * 
     * @type {Array<ValidationErrorDetails>}
     * @memberof ValidationError
     */
    errors: Array<ValidationErrorDetails>;
}

/**
 * Check if a given object implements the ValidationError interface.
 */
export function instanceOfValidationError(value: object): boolean {
    if (!('type' in value)) return false;
    if (!('errors' in value)) return false;
    return true;
}

export function ValidationErrorFromJSON(json: any): ValidationError {
    return ValidationErrorFromJSONTyped(json, false);
}

export function ValidationErrorFromJSONTyped(json: any, ignoreDiscriminator: boolean): ValidationError {
    if (json == null) {
        return json;
    }
    return {
        
        'type': json['type'],
        'errors': ((json['errors'] as Array<any>).map(ValidationErrorDetailsFromJSON)),
    };
}

export function ValidationErrorToJSON(value?: ValidationError | null): any {
    if (value == null) {
        return value;
    }
    return {
        
        'type': value['type'],
        'errors': ((value['errors'] as Array<any>).map(ValidationErrorDetailsToJSON)),
    };
}

