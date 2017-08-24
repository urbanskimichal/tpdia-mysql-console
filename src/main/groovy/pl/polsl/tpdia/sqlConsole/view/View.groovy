package pl.polsl.tpdia.sqlConsole.view

import groovy.swing.SwingBuilder
import pl.polsl.tpdia.sqlConsole.controller.Controller

import javax.swing.BorderFactory
import javax.swing.JFrame

class View {

    private def mainFrame
    private def settingsFrame

    private def settingsArea
    private def responseArea
    private def queryArea

    View(Controller controller) {
        createMainFrame(controller)
        createSettingsFrame(controller)
    }

    private void createMainFrame(Controller controller) {
        mainFrame = new SwingBuilder().frame(
                title: 'TPDiA MySql Console',
                size: [400, 300],
                locationRelativeTo: null,
                show: true,
                defaultCloseOperation: JFrame.EXIT_ON_CLOSE) {
            menuBar {
                menu(text: 'File') {
                    menuItem() {
                        action(name: 'Properties', closure: {
                            println "Show properites!"
                            settingsArea.text = controller.getSettings()
                            settingsFrame.show()
                        })
                    }
                }
                menu(text: 'Help') {
                    menuItem() {
                        action(name: 'About', closure: {
                            responseArea.text = controller.getAboutMessage()
                            println "About!"
                        })
                    }
                }
            }
            panel(border: BorderFactory.createLoweredBevelBorder()) {
                borderLayout()
                panel(
                        constraints: NORTH,
                        border: BorderFactory.createTitledBorder(
                                BorderFactory.createLoweredBevelBorder(), 'Query:')) {
                    borderLayout()
                    queryArea = textArea(
                            text: '',
                            constraints: CENTER
                    )
                    button(
                            text: 'Run',
                            constraints: EAST,
                            actionPerformed: {
                                def query = queryArea.text
                                def response = controller.runQuery(query)
                                responseArea.text = response
                                println "Query run!"
                            })
                }
                scrollPane(constraints: CENTER, border: BorderFactory.createTitledBorder(
                        BorderFactory.createLoweredBevelBorder(), 'Results:')) {
                    responseArea = textArea(
                            text: '',
                            editable: false
                    )
                }
            }
        }
    }

    void createSettingsFrame(Controller controller) {
        settingsFrame = new SwingBuilder().frame(
                title: 'DB settings',
                size: [300, 200],
                locationRelativeTo: null,
                show: false) {
            borderLayout()
            scrollPane(
                    border: BorderFactory.createTitledBorder(
                    BorderFactory.createLoweredBevelBorder(), 'Database settings:')) {
                settingsArea = textArea(
                        text: '',
                        editable: true
                )
            }
            button(
                    text: 'Save settings',
                    constraints: SOUTH,
                    actionPerformed: {
                responseArea.text = controller.saveSettings(settingsArea.text)
                dispose() //close window
                println "Settings saved!"
            })
        }
    }
}