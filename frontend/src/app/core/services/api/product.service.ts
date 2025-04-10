import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { environment } from '../../../../enviroments/environment';
import { Product } from '../../models/product.model';

@Injectable({
  providedIn: 'root'
})
export class ProductService {
  private apiUrl = `${environment.apiUrl}/produtos`;

  constructor(private http: HttpClient) {}

  createProduct(product: Product): Observable<Product> {
    return this.http.post<Product>(this.apiUrl, {
      ...product,
      productStatus: product.productStatus.toUpperCase()
    })
      .pipe(catchError(this.handleError));
  }

  getProductById(id: number): Observable<Product> {
    return this.http.get<Product>(`${this.apiUrl}/${id}`)
      .pipe(catchError(this.handleError));
  }

  getAllProducts(): Observable<Product[]> {
    return this.http.get<Product[]>(this.apiUrl)
      .pipe(catchError(this.handleError));
  }

  updateProduct(id: number, product: Product): Observable<Product> {
    return this.http.put<Product>(`${this.apiUrl}/${id}`, {
      ...product,
      productStatus: product.productStatus.toUpperCase()
    })
      .pipe(catchError(this.handleError));
  }

  deleteProduct(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`)
      .pipe(
        catchError(this.handleError)
      );
  }

  getProductByCode(code: string): Observable<Product[]> {
    return this.http.get<Product[]>(`${this.apiUrl}/buscar?productCode=${encodeURIComponent(code)}`)
      .pipe(catchError(this.handleError));
  }

  private handleError(error: HttpErrorResponse) {
    let errorMessage = 'Ocorreu um erro desconhecido';
    
    if (error.error instanceof ErrorEvent) {
      errorMessage = `Erro: ${error.error.message}`;
    } else {
      if (typeof error.error === 'object' && error.error !== null && 'message' in error.error) {
        errorMessage = error.error.message;
      } else if (typeof error.error === 'string') {
        errorMessage = error.error;
        
        try {
          const errorBody = JSON.parse(error.error);
          
          if (errorBody && errorBody.message) {
            errorMessage = errorBody.message;
          }
        } catch (e) {
        }
      }
    }
    
    return throwError(() => new Error(errorMessage));
  }
}