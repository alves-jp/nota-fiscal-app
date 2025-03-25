import { Component } from '@angular/core';

@Component({
  selector: 'app-not-found',
  template: `
    <div class="not-found-container">
      <h1>Página não encontrada</h1>
      <p>A página que você está procurando não existe.</p>
      <a routerLink="/">Voltar para a página inicial</a>
    </div>
  `,
  styles: [
    `
    .not-found-container {
      text-align: center;
      margin-top: 50px;
    }
    h1 {
      color: red;
    }
    p {
      font-size: 18px;
    }
    a {
      color: blue;
      text-decoration: none;
    }
    a:hover {
      text-decoration: underline;
    }
    `
  ]
})
export class NotFoundComponent { }