import { useEffect, useMemo, useState } from 'react'
import { getUploadedImages, uploadCropImage } from '../services/uploadService'

const API_ORIGIN = (import.meta.env.VITE_API_URL || 'http://localhost:5000/api').replace(/\/api\/?$/, '')

function ImageUpload() {
  const [selectedFile, setSelectedFile] = useState(null)
  const [images, setImages] = useState([])
  const [analysis, setAnalysis] = useState('')
  const [error, setError] = useState('')
  const [loading, setLoading] = useState(false)
  const [fetching, setFetching] = useState(true)

  const previewUrl = useMemo(() => {
    if (!selectedFile) {
      return ''
    }

    return URL.createObjectURL(selectedFile)
  }, [selectedFile])

  useEffect(() => {
    return () => {
      if (previewUrl) {
        URL.revokeObjectURL(previewUrl)
      }
    }
  }, [previewUrl])

  useEffect(() => {
    const loadImages = async () => {
      setError('')

      try {
        const results = await getUploadedImages()
        setImages(results)
      } catch (apiError) {
        setError(apiError.response?.data?.message || 'Unable to load uploaded images.')
      } finally {
        setFetching(false)
      }
    }

    loadImages()
  }, [])

  const handleFileChange = (event) => {
    const file = event.target.files?.[0]
    setAnalysis('')
    setError('')

    if (!file) {
      setSelectedFile(null)
      return
    }

    const allowedTypes = ['image/jpeg', 'image/png']

    if (!allowedTypes.includes(file.type)) {
      setSelectedFile(null)
      setError('Please choose a JPG, JPEG, or PNG image.')
      return
    }

    if (file.size > 5 * 1024 * 1024) {
      setSelectedFile(null)
      setError('Image size must be 5 MB or less.')
      return
    }

    setSelectedFile(file)
  }

  const handleSubmit = async (event) => {
    event.preventDefault()
    setError('')
    setAnalysis('')

    if (!selectedFile) {
      setError('Please choose a crop image first.')
      return
    }

    setLoading(true)

    try {
      const result = await uploadCropImage(selectedFile)
      setImages((current) => [result.image, ...current])
      setAnalysis(result.analysis)
      setSelectedFile(null)
      event.target.reset()
    } catch (apiError) {
      setError(apiError.response?.data?.message || 'Unable to upload image right now.')
    } finally {
      setLoading(false)
    }
  }

  return (
    <section className="grid gap-6">
      <div>
        <h1 className="text-3xl font-bold text-[#17211b]">Crop Image Upload</h1>
        <p className="mt-2 text-[#526056]">Upload JPG, JPEG, or PNG crop photos for future disease analysis.</p>
      </div>

      <div className="grid gap-6 lg:grid-cols-[0.9fr_1.1fr]">
        <form className="rounded border border-[#d8e1d2] bg-white p-5 shadow-sm" onSubmit={handleSubmit}>
          <label className="grid gap-2 text-sm font-medium text-[#334137]">
            Crop image
            <input
              accept=".jpg,.jpeg,.png,image/jpeg,image/png"
              className="rounded border border-[#c9d6c4] px-3 py-2"
              onChange={handleFileChange}
              type="file"
            />
          </label>

          {previewUrl && (
            <img
              alt="Selected crop preview"
              className="mt-4 aspect-video w-full rounded border border-[#d8e1d2] object-cover"
              src={previewUrl}
            />
          )}

          {analysis && <p className="mt-4 rounded border border-green-200 bg-green-50 px-3 py-2 text-sm text-green-700">{analysis}</p>}
          {error && <p className="mt-4 rounded border border-red-200 bg-red-50 px-3 py-2 text-sm text-red-700">{error}</p>}

          <button className="mt-4 rounded bg-[#1d5f35] px-4 py-2 font-semibold text-white disabled:opacity-60" disabled={loading} type="submit">
            {loading ? 'Uploading...' : 'Upload image'}
          </button>
        </form>

        <div className="rounded border border-[#d8e1d2] bg-white p-5 shadow-sm">
          <h2 className="font-semibold text-[#1d5f35]">Uploaded Images</h2>
          {fetching && <p className="mt-4 text-sm text-[#526056]">Loading images...</p>}

          {!fetching && images.length === 0 ? (
            <p className="mt-4 text-sm text-[#526056]">No images uploaded yet.</p>
          ) : (
            <div className="mt-4 grid gap-4 sm:grid-cols-2">
              {images.map((image) => (
                <article className="rounded border border-[#d8e1d2] p-3" key={image._id}>
                  <img
                    alt={image.imageName}
                    className="aspect-video w-full rounded object-cover"
                    src={`${API_ORIGIN}${image.imagePath}`}
                  />
                  <h3 className="mt-3 break-words text-sm font-semibold text-[#17211b]">{image.imageName}</h3>
                  <p className="mt-1 text-xs text-[#69756c]">{new Date(image.createdAt).toLocaleString()}</p>
                  <p className="mt-2 text-xs leading-5 text-[#526056]">{image.analysis}</p>
                </article>
              ))}
            </div>
          )}
        </div>
      </div>
    </section>
  )
}

export default ImageUpload
