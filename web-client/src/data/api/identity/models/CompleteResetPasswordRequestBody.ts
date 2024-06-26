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
/**
 * 
 * @export
 * @interface CompleteResetPasswordRequestBody
 */
export interface CompleteResetPasswordRequestBody {
    /**
     * 
     * @type {string}
     * @memberof CompleteResetPasswordRequestBody
     */
    operationId: string;
    /**
     * 
     * @type {string}
     * @memberof CompleteResetPasswordRequestBody
     */
    newPassword: string;
    /**
     * 
     * @type {string}
     * @memberof CompleteResetPasswordRequestBody
     */
    resetPasswordToken: string;
}

/**
 * Check if a given object implements the CompleteResetPasswordRequestBody interface.
 */
export function instanceOfCompleteResetPasswordRequestBody(value: object): boolean {
    if (!('operationId' in value)) return false;
    if (!('newPassword' in value)) return false;
    if (!('resetPasswordToken' in value)) return false;
    return true;
}

export function CompleteResetPasswordRequestBodyFromJSON(json: any): CompleteResetPasswordRequestBody {
    return CompleteResetPasswordRequestBodyFromJSONTyped(json, false);
}

export function CompleteResetPasswordRequestBodyFromJSONTyped(json: any, ignoreDiscriminator: boolean): CompleteResetPasswordRequestBody {
    if (json == null) {
        return json;
    }
    return {
        
        'operationId': json['operationId'],
        'newPassword': json['newPassword'],
        'resetPasswordToken': json['resetPasswordToken'],
    };
}

export function CompleteResetPasswordRequestBodyToJSON(value?: CompleteResetPasswordRequestBody | null): any {
    if (value == null) {
        return value;
    }
    return {
        
        'operationId': value['operationId'],
        'newPassword': value['newPassword'],
        'resetPasswordToken': value['resetPasswordToken'],
    };
}

