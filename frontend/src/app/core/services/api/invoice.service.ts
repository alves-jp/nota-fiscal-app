import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { environment } from '../../../../enviroments/environment';
import { Invoice, InvoiceDTO, InvoiceResponseDTO } from '../../models/invoice.model';

@Injectable({
  providedIn: 'root'
})
export class InvoiceService {
  private apiUrl = `${environment.apiUrl}/notas-fiscais`;

  constructor(private http: HttpClient) {}

  createInvoice(invoice: InvoiceDTO): Observable<Invoice> {
    return this.http.post<Invoice>(this.apiUrl, invoice)
      .pipe(catchError(this.handleError));
  }

  getInvoiceById(id: number): Observable<InvoiceResponseDTO> {
    return this.http.get<InvoiceResponseDTO>(`${this.apiUrl}/${id}`)
      .pipe(catchError(this.handleError));
  }

  getAllInvoices(): Observable<InvoiceResponseDTO[]> {
    return this.http.get<InvoiceResponseDTO[]>(this.apiUrl)
      .pipe(catchError(this.handleError));
  }

  updateInvoice(id: number, invoice: InvoiceDTO): Observable<Invoice> {
    return this.http.put<Invoice>(`${this.apiUrl}/${id}`, invoice)
      .pipe(catchError(this.handleError));
  }

  deleteInvoice(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`)
      .pipe(catchError(this.handleError));
  }

  getInvoiceByNumber(number: string): Observable<Invoice[]> {
    return this.http.get<Invoice[]>(`${this.apiUrl}/buscar?invoiceNumber=${encodeURIComponent(number)}`)
      .pipe(catchError(this.handleError));
  }

  private handleError(error: HttpErrorResponse) {
    let errorMessage = 'Ocorreu um erro desconhecido';
    
    if (error.error instanceof ErrorEvent) {
      errorMessage = `Erro: ${error.error.message}`;
    } else {
      if (error.error && error.error.message) {
        errorMessage = error.error.message;
      } else if (error.status === 400 || error.status === 404) {
        try {
          const errorBody = JSON.parse(error.error);
          errorMessage = errorBody.message || errorMessage;
        } catch (e) {
        }
      }
    }
    
    return throwError(() => new Error(errorMessage));
  }
}