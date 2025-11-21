# InventoryTrack

InventoryTrack is an open-source inventory management system developed in Java with a modern web frontend and secure authentication. This project is designed to help users and organizations keep track of their stock, manage inventory operations efficiently, and integrate seamlessly with robust database and authentication solutions. InventoryTrack is fully deployable on Kubernetes for scalable, cloud-native operation.

## Features

- **Inventory Management:** Track and manage inventory efficiently.
- **Java Backend:** Main backend system in Java with Maven build support.
- **Next.js Frontend:** Dedicated Next.js frontend server for a modern web UI.
- **Keycloak Authentication:** Uses a Keycloak authentication server (client flow) to secure API and frontend access.
- **PostgreSQL Integration:** Robust database storage using PostgreSQL.
- **Docker Support:** Containerized setup with provided Dockerfile.
- **Kubernetes Deployment:** Ready-made Kubernetes configuration using Helm, with support for Service (ClusterIP & NodePort), and ConfigMaps for environment management.
- **Extensible Code Structure:** Organized following best practices for extensibility.
- **MIT Licensed**

## Repository Structure

- `.mvn/`            – Maven wrapper resources
- `src/`             – Backend Java source code
- `supabase/`        – (presumed) Supabase integration or config files
- `Dockerfile`       – Docker configuration for backend service
- `Run PostgreSQL.bat` – Batch script for running PostgreSQL locally
- `pom.xml`          – Maven project configuration and dependencies
- `LICENSE`          – Project license (MIT)
- (Frontend code is in a separate Next.js project/service)

## System Architecture

- **Backend:** Java (Maven), PostgreSQL
- **Frontend:** Next.js (JavaScript/TypeScript)
- **Authentication:** Keycloak, using client flow
- **Deployment:** Kubernetes (Helm), including Service (Cluster IP and NodePort), ConfigMaps for flexible configuration

## Getting Started

### Prerequisites

- Java (JDK 11+)
- Node.js & npm (for Next.js frontend)
- Maven 3.x
- PostgreSQL database
- Docker
- Kubernetes cluster
- Helm CLI
- Keycloak server

### Installation

1. **Clone Backend Repository:**
    ```sh
    git clone https://github.com/ahmed84232/InventoryTrack.git
    cd InventoryTrack
    ```

2. **Clone Frontend Repository (if separate):**
    ```sh
    # Replace with actual repo/location of Next.js frontend
    git clone <frontend-repo-url> inventorytrack-frontend
    cd inventorytrack-frontend
    ```

3. **Configure Keycloak Auth:**
    - Install and run a Keycloak server (see Keycloak docs).
    - Set up a client for InventoryTrack (use client credentials flow).
    - Configure both backend and frontend to point to your Keycloak server.

4. **Set Up Database:**
    - Deploy PostgreSQL, or run locally (see `Run PostgreSQL.bat`).

5. **Build and Run Backend (Java):**
    ```sh
    mvn clean install
    mvn spring-boot:run
    ```

6. **Build and Run Frontend (Next.js):**
    ```sh
    cd inventorytrack-frontend
    npm install
    npm run dev
    ```

7. **Deployment on Kubernetes:**
    - Use provided Helm charts and manifests to deploy all services.
    - Services include:
        - Backend Java API (Cluster IP or NodePort)
        - Next.js frontend (if containerized)
        - Keycloak (if self-hosted)
        - PostgreSQL
    - Configurations are managed with Kubernetes ConfigMaps.

### Deployment Example (Kubernetes/Helm)

```sh
# Backend
cd k8s/helm/backend
helm install inventory-backend .

# Frontend (if containerized)
cd k8s/helm/frontend
helm install inventory-frontend .

# Keycloak and PostgreSQL may be installed similarly or from their own Helm charts
```

> Customize ConfigMaps and secrets as needed for your environment.

## Contributing

Pull requests are welcome! Please open an issue first to discuss your ideas.

## License

This project is licensed under the MIT License. See the [LICENSE](https://github.com/ahmed84232/InventoryTrack/blob/master/LICENSE) file for details.

## Author

- [ahmed84232](https://github.com/ahmed84232)

---
For further information and full source code, visit the [GitHub repository](https://github.com/ahmed84232/InventoryTrack).
