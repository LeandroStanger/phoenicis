/*
 * Copyright (C) 2015-2017 PÂRIS Quentin
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package org.phoenicis.javafx.controller.containers;

import javafx.application.Platform;
import org.phoenicis.containers.ContainersManager;
import org.phoenicis.containers.dto.ContainerDTO;
import org.phoenicis.containers.dto.WinePrefixContainerDTO;
import org.phoenicis.containers.ContainerEngineController;
import org.phoenicis.engines.EngineSetting;
import org.phoenicis.engines.EngineSettingsManager;
import org.phoenicis.engines.EngineToolsManager;
import org.phoenicis.javafx.views.common.ConfirmMessage;
import org.phoenicis.javafx.views.common.ErrorMessage;
import org.phoenicis.javafx.views.mainwindow.containers.ContainerPanel;
import org.phoenicis.javafx.views.mainwindow.containers.ContainersView;
import org.phoenicis.repository.RepositoryManager;
import org.phoenicis.repository.dto.ApplicationDTO;
import org.phoenicis.repository.dto.RepositoryDTO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.phoenicis.configuration.localisation.Localisation.tr;

public class ContainersController {
    private final ContainersView containersView;
    private final ContainersManager containersManager;
    private EngineSettingsManager engineSettingsManager;
    private final EngineToolsManager engineToolsManager;
    private Map<String, List<EngineSetting>> engineSettings; // engine settings per engine
    private Map<String, ApplicationDTO> engineTools; // engine tools per engine

    public ContainersController(ContainersView containersView,
            ContainersManager containersManager,
            ContainerEngineController containerEngineController,
            RepositoryManager repositoryManager,
            EngineSettingsManager engineSettingsManager,
            EngineToolsManager engineToolsManager) {
        this.containersView = containersView;
        this.containersManager = containersManager;
        this.engineSettingsManager = engineSettingsManager;
        this.engineToolsManager = engineToolsManager;

        this.engineSettings = new HashMap<>();
        repositoryManager.addCallbacks(this::updateEngineSettings,
                e -> Platform.runLater(
                        () -> new ErrorMessage(tr("Loading engine settings failed."), e, this.containersView)));

        this.engineTools = new HashMap<>();
        repositoryManager.addCallbacks(this::updateEngineTools,
                e -> Platform
                        .runLater(() -> new ErrorMessage(tr("Loading engine tools failed."), e, this.containersView)));

        containersView.setOnSelectionChanged(event -> {
            if (containersView.isSelected()) {
                loadContainers();
            }
        });

        containersView.setOnSelectContainer((ContainerDTO containerDTO) -> {
            // TODO: better way to get engine ID
            final String engineId = containerDTO.getEngine().toLowerCase();
            final ContainerPanel panel = new ContainerPanel(
                    (WinePrefixContainerDTO) containerDTO,
                    engineToolsManager,
                    Optional.ofNullable(engineSettings.get(engineId)),
                    Optional.ofNullable(engineTools.get(engineId)),
                    containerEngineController);

            panel.setOnDeletePrefix(
                    containerToDelete -> new ConfirmMessage(tr("Delete {0} container", containerToDelete.getName()),
                            tr("Are you sure you want to delete the {0} container?", containerToDelete.getName()),
                            this.containersView.getContent().getScene().getWindow())
                                    .ask(() -> {
                                        containersManager.deleteContainer(containerToDelete,
                                                e -> Platform.runLater(
                                                        () -> new ErrorMessage("Error", e, this.containersView)
                                                                .show()));
                                        loadContainers();
                                    }));

            panel.setOnClose(containersView::closeDetailsView);

            Platform.runLater(() -> containersView.showDetailsView(panel));
            // });
        });
    }

    public ContainersView getView() {
        return containersView;
    }

    public void loadContainers() {
        this.containersView.showWait();
        this.containersManager.fetchContainers(containersView::populate,
                e -> this.containersView.showFailure(tr("Loading containers failed."), Optional
                        .of(e)));
    }

    private void updateEngineSettings(RepositoryDTO repositoryDTO) {
        this.engineSettingsManager.fetchAvailableEngineSettings(repositoryDTO,
                engineSettings -> Platform.runLater(() -> this.engineSettings = engineSettings),
                e -> Platform
                        .runLater(() -> new ErrorMessage(tr("Loading engine tools failed."), e, this.containersView)));
    }

    private void updateEngineTools(RepositoryDTO repositoryDTO) {
        this.engineToolsManager.fetchAvailableEngineTools(repositoryDTO,
                engineTools -> Platform.runLater(() -> this.engineTools = engineTools));
    }
}
