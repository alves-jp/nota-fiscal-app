import { Injectable } from '@angular/core';
import { HttpClient, HttpErrorResponse } from '@angular/common/http';
import { Observable, throwError } from 'rxjs';
import { catchError } from 'rxjs/operators';
import { environment } from '../../../../enviroments/environment';
import { InvoiceDTO, InvoiceResponseDTO } from '../../models/invoice.model';

@Injectable({
  providedIn: 'root'
})
export class InvoiceService {
  private apiUrl = `${environment.apiUrl}/notas-fiscais`;

  constructor(private http: HttpClient) {}

  createInvoice(invoice: InvoiceDTO): Observable<InvoiceResponseDTO> {
    const payload = {
      invoiceNumber: invoice.invoiceNumber,
      issueDate: this.formatDateForBackend(invoice.issueDate),
      supplierId: invoice.supplierId,
      address: invoice.address
    };

    return this.http.post<InvoiceResponseDTO>(this.apiUrl, payload)
      .pipe(
        catchError(this.handleError)
      );
  }

  getInvoiceById(id: number): Observable<InvoiceResponseDTO> {
    return this.http.get<InvoiceResponseDTO>(`${this.apiUrl}/${id}`)
      .pipe(
        catchError(this.handleError)
      );
  }

  getAllInvoices(): Observable<InvoiceResponseDTO[]> {
    return this.http.get<InvoiceResponseDTO[]>(this.apiUrl)
      .pipe(
        catchError(this.handleError)
      );
  }

  updateInvoice(id: number, invoice: InvoiceDTO): Observable<InvoiceResponseDTO> {
    const payload = {
      invoiceNumber: invoice.invoiceNumber,
      issueDate: this.formatDateForBackend(invoice.issueDate),
      supplierId: invoice.supplierId,
      address: invoice.address
    };

    return this.http.put<InvoiceResponseDTO>(`${this.apiUrl}/${id}`, payload)
      .pipe(
        catchError(this.handleError)
      );
  }

  deleteInvoice(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/${id}`)
      .pipe(
        catchError(this.handleError)
      );
  }

  getInvoiceByNumber(number: string): Observable<InvoiceResponseDTO[]> {
    return this.http.get<InvoiceResponseDTO[]>(
      `${this.apiUrl}/buscar?invoiceNumber=${encodeURIComponent(number)}`
    ).pipe(
      catchError(this.handleError)
    );
  }

  private formatDateForBackend(date: Date | string | null): string | null {
    if (!date) {
      return null; // Backend vai usar `now()`
    }
    const parsedDate = new Date(date);
    return parsedDate.toISOString();
  }
  
  private handleError(error: HttpErrorResponse) {
    let errorMessage = 'Ocorreu um erro desconhecido';
    
    if (error.error instanceof ErrorEvent) {
      errorMessage = `Erro: ${error.error.message}`;
    } else {
      if (error.status === 0) {
        errorMessage = 'Não foi possível conectar ao servidor. Verifique sua conexão.';
      } else if (error.status === 400) {
        errorMessage = error.error?.message || 'Dados inválidos enviados ao servidor';
      } else if (error.status === 404) {
        errorMessage = error.error?.message || 'Recurso não encontrado';
      } else if (error.status === 500) {
        errorMessage = error.error?.message || 'Erro interno no servidor';
      } else {
        errorMessage = `Erro ${error.status}: ${error.message}`;
      }
    }
    
    return throwError(() => new Error(errorMessage));
  }
}