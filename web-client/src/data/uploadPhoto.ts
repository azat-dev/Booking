const uploadPhoto = async (url: string, params: Record<string, string>, photo: File): Promise<boolean> => {

    const formData = new FormData();

    for (const key in params) {
        formData.append(key, params[key]);
    }

    formData.append('Content-Type', photo.type);
    formData.append('file', photo);

    const result = await fetch(url, {
        method: "POST",
        body: formData
    });

    return result.status === 200 || result.status === 201 || result.status === 204;
}

export default uploadPhoto;