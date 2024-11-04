package transactions.com.repository;

import transactions.com.entity.Settings;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SettingsRepository extends JpaRepository<Settings, Long> {
    // Pas besoin d'ajouter des méthodes supplémentaires pour les opérations de base
}
