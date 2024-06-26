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
import type { EmailVerificationStatus } from './EmailVerificationStatus';
import {
    EmailVerificationStatusFromJSON,
    EmailVerificationStatusFromJSONTyped,
    EmailVerificationStatusToJSON,
} from './EmailVerificationStatus';
import type { FullName } from './FullName';
import {
    FullNameFromJSON,
    FullNameFromJSONTyped,
    FullNameToJSON,
} from './FullName';
import type { PhotoPath } from './PhotoPath';
import {
    PhotoPathFromJSON,
    PhotoPathFromJSONTyped,
    PhotoPathToJSON,
} from './PhotoPath';

/**
 * 
 * @export
 * @interface PersonalUserInfo
 */
export interface PersonalUserInfo {
    /**
     * 
     * @type {string}
     * @memberof PersonalUserInfo
     */
    id: string;
    /**
     * 
     * @type {FullName}
     * @memberof PersonalUserInfo
     */
    fullName: FullName;
    /**
     * 
     * @type {string}
     * @memberof PersonalUserInfo
     */
    email: string;
    /**
     * 
     * @type {PhotoPath}
     * @memberof PersonalUserInfo
     */
    photo?: PhotoPath;
    /**
     * 
     * @type {EmailVerificationStatus}
     * @memberof PersonalUserInfo
     */
    emailVerificationStatus: EmailVerificationStatus;
}

/**
 * Check if a given object implements the PersonalUserInfo interface.
 */
export function instanceOfPersonalUserInfo(value: object): boolean {
    if (!('id' in value)) return false;
    if (!('fullName' in value)) return false;
    if (!('email' in value)) return false;
    if (!('emailVerificationStatus' in value)) return false;
    return true;
}

export function PersonalUserInfoFromJSON(json: any): PersonalUserInfo {
    return PersonalUserInfoFromJSONTyped(json, false);
}

export function PersonalUserInfoFromJSONTyped(json: any, ignoreDiscriminator: boolean): PersonalUserInfo {
    if (json == null) {
        return json;
    }
    return {
        
        'id': json['id'],
        'fullName': FullNameFromJSON(json['fullName']),
        'email': json['email'],
        'photo': json['photo'] == null ? undefined : PhotoPathFromJSON(json['photo']),
        'emailVerificationStatus': EmailVerificationStatusFromJSON(json['emailVerificationStatus']),
    };
}

export function PersonalUserInfoToJSON(value?: PersonalUserInfo | null): any {
    if (value == null) {
        return value;
    }
    return {
        
        'id': value['id'],
        'fullName': FullNameToJSON(value['fullName']),
        'email': value['email'],
        'photo': PhotoPathToJSON(value['photo']),
        'emailVerificationStatus': EmailVerificationStatusToJSON(value['emailVerificationStatus']),
    };
}

