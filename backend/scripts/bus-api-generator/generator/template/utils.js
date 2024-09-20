export const camelCaseToScreamingSnakeCase = (str) => {
    return str.replace(/([A-Z])/g, '_$1').toUpperCase();
}

export const capitalize = (str) => str.charAt(0).toUpperCase() + str.slice(1);

export const removeSlashes = (text) => text.replace(/\//g, '').replace(/\\/g, '');

export const convertChannelIdToConstantName = (id) => {
    return camelCaseToScreamingSnakeCase(id.replace(/\\/g, '_').replace(/\//g, '_'));
}

export const getChannelServiceId = (channel) => channel.extensions().all().find(i => i.id() === 'x-service')?.value();

export const convertChannelIdToPackageName = (id) => {
    return id.replace(/\\/g, '.').replace(/\//g, '.').toLowerCase();
}