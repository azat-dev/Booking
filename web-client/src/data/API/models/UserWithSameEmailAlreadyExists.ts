/* tslint:disable */
/* eslint-disable */
/**
 * Demo Booking API
 * Describes the API of Daily Tasks
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
 * @interface UserWithSameEmailAlreadyExists
 */
export interface UserWithSameEmailAlreadyExists {
    /**
     * 
     * @type {string}
     * @memberof UserWithSameEmailAlreadyExists
     */
    code: string;
    /**
     * 
     * @type {string}
     * @memberof UserWithSameEmailAlreadyExists
     */
    message: string;
}

/**
 * Check if a given object implements the UserWithSameEmailAlreadyExists interface.
 */
export function instanceOfUserWithSameEmailAlreadyExists(value: object): boolean {
    if (!('code' in value)) return false;
    if (!('message' in value)) return false;
    return true;
}

export function UserWithSameEmailAlreadyExistsFromJSON(json: any): UserWithSameEmailAlreadyExists {
    return UserWithSameEmailAlreadyExistsFromJSONTyped(json, false);
}

export function UserWithSameEmailAlreadyExistsFromJSONTyped(json: any, ignoreDiscriminator: boolean): UserWithSameEmailAlreadyExists {
    if (json == null) {
        return json;
    }
    return {
        
        'code': json['code'],
        'message': json['message'],
    };
}

export function UserWithSameEmailAlreadyExistsToJSON(value?: UserWithSameEmailAlreadyExists | null): any {
    if (value == null) {
        return value;
    }
    return {
        
        'code': value['code'],
        'message': value['message'],
    };
}
