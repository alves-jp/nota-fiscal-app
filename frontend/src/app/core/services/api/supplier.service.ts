import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { environment } from '../../../../enviroments/environment';
import { Supplier } from '../../models/supplier.model';

@Injectable({
  providedIn: 'root'
})
export class SupplierService {
  private apiUrl = `${environment.apiUrl}/fornecedores`;

  constructor(private http: HttpClient) {}

  createSupplier(supplier: Supplier): Observable<Supplier> {
    return this.http.post<Supplier>(this.apiUrl, supplier)
      .pipe(catchError(this.handleError));
  }

  getSupplierById(id: number): Observable<Supplier> {
    return this.http.get<Supplier>(`${this.apiUrl}/${id}`)
      .pipe(catchError(this.handleError));
  }

  getAllSuppliers(): Observable<Supplier[]> {
    return this.http.get<Supplier[]>(this.apiUrl)
      .pipe(catchError(this.handleError));
  }

  getSuppliersByName(name: string): Observable<Supplier[]> {
    return this.http.get<Supplier[]>(`${this.apiUrl}/buscar?companyName=${encodeURIComponent(name)}`)
      .pipe(catchError(this.handleError));
  }

  updateSupplier(id: number, supplier: Supplier): Observable<Supplier> {
    return this.http.put<Supplier>(`${this.apiUrl}/${id}`, supplier)
      .pipe(catchError(this.handleError));
  }

  deleteSupplier(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`)
      .pipe(catchError(this.handleError));
  }

  private handleError(error: HttpErrorResponse) {
    let errorMessage = 'Ocorreu um erro desconhecido';
    
    if (error.error instanceof ErrorEvent) {
      errorMessage = `Erro: ${error.error.message}`;
      
    } else {
      if (typeof error.error === 'string') {
        errorMessage = error.error;

      } else if (error.error?.message) {
        errorMessage = error.error.message;

      } else if (error.message) {
        errorMessage = error.message;
      }
    }
    
    return throwError(() => new Error(errorMessage));
  }
}