export interface User {
  id: string;
  username: string;
  email: string;
  avatar?: string;
  createdAt: Date;
}

export interface Movie {
  id: string;
  title: string;
  description: string;
  releaseDate: Date;
  genre: string[];
  director: string;
  cast: string[];
  posterUrl?: string;
  duration: number; // in minutes
  imdbRating?: number;
  averageRating: number;
  totalRatings: number;
}

export interface Rating {
  id: string;
  userId: string;
  movieId: string;
  score: number; // 1-10
  review?: string;
  createdAt: Date;
  updatedAt: Date;
}

export interface Recommendation {
  id: string;
  userId: string;
  movieId: string;
  score: number; // relevance score
  reason: string;
  createdAt: Date;
}

export interface Watchlist {
  id: string;
  userId: string;
  movieId: string;
  addedAt: Date;
}

export interface PaginatedResponse<T> {
  data: T[];
  total: number;
  page: number;
  pageSize: number;
}

export interface SearchFilter {
  query?: string;
  genre?: string[];
  releaseYear?: number;
  minRating?: number;
  sortBy?: 'title' | 'rating' | 'releaseDate';
  page?: number;
  pageSize?: number;
}

export interface AuthResponse {
  token: string;
  refreshToken?: string;
  user: User;
}

export interface AuthRequest {
  email: string;
  password: string;
}

export interface RegisterRequest {
  username: string;
  email: string;
  password: string;
  confirmPassword: string;
}
