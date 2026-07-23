import { ChangeDetectionStrategy, Component } from '@angular/core';

@Component({
  selector: 'app-user-loans-placeholder',
  template: `
    <main class="placeholder">
      <div class="mk-card-elevated" style="max-width:42rem;">
        <p class="mk-eyebrow">Área de usuario</p>
        <h2 class="mk-h2" style="margin-top:0.25rem;">Mis préstamos</h2>
        <p class="mk-subtitle" style="margin-top:0.5rem;">
          La gestión de solicitudes se implementará en la Épica 2.
        </p>
      </div>
    </main>
  `,
  styles: `
    :host { display: block; }
    .placeholder {
      min-height: 100vh;
      display: grid;
      place-items: center;
      padding: 2rem;
      background:
        radial-gradient(circle at 15% 20%, rgba(107, 69, 188, 0.05), transparent 30rem),
        var(--mk-bg-light);
    }
  `,
  changeDetection: ChangeDetectionStrategy.OnPush,
})
export class UserLoansPlaceholderComponent {}
