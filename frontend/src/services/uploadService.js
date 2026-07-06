import api from './api'

export const uploadCropImage = async (file) => {
  const formData = new FormData()
  formData.append('image', file)

  const { data } = await api.post('/upload', formData, {
    headers: {
      'Content-Type': 'multipart/form-data',
    },
  })

  return data
}

export const getUploadedImages = async () => {
  const { data } = await api.get('/upload')
  return data.images
}
